package br.edu.ifpb.dac.groupd.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.ifpb.dac.groupd.dto.LocationDto;
import br.edu.ifpb.dac.groupd.dto.post.LocationPostDto;
import br.edu.ifpb.dac.groupd.exception.BraceletNotFoundException;
import br.edu.ifpb.dac.groupd.exception.LocationCreationDateInFutureException;
import br.edu.ifpb.dac.groupd.exception.LocationNotFoundException;
import br.edu.ifpb.dac.groupd.model.Location;
import br.edu.ifpb.dac.groupd.service.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationResource {
	@Autowired
	private LocationService locationService;
	@Autowired
	private ModelMapper mapper;
	
	
	@PostMapping
	@ResponseStatus(code=HttpStatus.CREATED)
	public ResponseEntity<?> create(
			@Valid
			@RequestBody
			LocationPostDto postDto,
			HttpServletResponse response
			) {
		try {
			Location location = locationService.create(postDto);
			
			LocationDto dto = mapToDto(location);
			
			URI uri = ServletUriComponentsBuilder
					.fromCurrentRequestUri()
					.path("/?id={pulseiraId}")
					.buildAndExpand(location.getId())
					.toUri();
			
			return ResponseEntity.created(uri).body(dto);
		} catch (BraceletNotFoundException | LocationCreationDateInFutureException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	@GetMapping
	public ResponseEntity<?> findById(@RequestParam(name="id", required=true) Long id){
		try {
			Location location = locationService.findById(id);
			
			LocationDto dto = mapToDto(location);
			
			return ResponseEntity.ok(dto);
		} catch (LocationNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
		
	}
	@GetMapping("/{pulseiraId}")
	public ResponseEntity<?> findByBraceletId(@PathVariable("pulseiraId") Long pulseiraId){
		try {
			List<Location> locations = locationService.findByBraceletId(pulseiraId);
			
			List<LocationDto> dtos = locations
					.stream()
					.map(
						location->{
							LocationDto dto = mapToDto(location);
							dto.setBraceletId(location.getBracelet().getId());
							return dto;
						}
					)
					.toList();
			
			return ResponseEntity.ok(dtos);
		} catch (BraceletNotFoundException exception) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
		}
	}
	
	public LocationDto mapToDto(Location location){
		LocationDto dto = mapper.map(location, LocationDto.class);
		
		dto.setBraceletId(location.getBracelet().getId());
		
		return dto;
	 }
	 
}
