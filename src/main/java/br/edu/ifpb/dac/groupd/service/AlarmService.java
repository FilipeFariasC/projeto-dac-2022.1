package br.edu.ifpb.dac.groupd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;

import br.edu.ifpb.dac.groupd.exception.AlarmNotFoundException;
import br.edu.ifpb.dac.groupd.model.Alarm;
import br.edu.ifpb.dac.groupd.model.Fence;
import br.edu.ifpb.dac.groupd.model.Location;
import br.edu.ifpb.dac.groupd.repository.AlarmRepository;

@Service
public class AlarmService {
	
	@Autowired
	private AlarmRepository alarmRepository;
	
	public Alarm saveAlarm(Location location, Fence fence) {
		Alarm alarm = new Alarm();
		
		alarm.setFence(fence);
		alarm.setLocation(location);
		alarm.setSeen(false);
		
		return alarmRepository.save(alarm);

	}
	
	public List<Alarm> getAll(){
		return alarmRepository.findAll();
	}
	
	public List<Alarm> findByFenceId(Long fenceId){
		return alarmRepository.findByFence(fenceId);
	}
	public List<Alarm> findByBraceletId(Long braceletId){
		return alarmRepository.findByBracelet(braceletId);
	}
	public Alarm findByLocationId(Long locationId) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findByLocation(locationId);
		
		if(register.isEmpty())
			throw new AlarmNotFoundException(String.format("Não foi encontrado alarmes para a location de identificador %d", locationId));
		
		return register.get();
	}
	
	public Alarm alarmSeen(Long idAlarm) throws AlarmNotFoundException {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);
		
		if(register.isEmpty()) {
			throw new AlarmNotFoundException(idAlarm);
		}
		Alarm alarm = register.get();
		alarm.setSeen(true);
		
		return alarmRepository.save(alarm);
	}
	
	public Alarm findAlarmById(Long idAlarm) throws AlarmNotFoundException  {
		Optional<Alarm> register = alarmRepository.findById(idAlarm);
		
		if(register.isEmpty()) 
			throw new AlarmNotFoundException(idAlarm);
		
		
		return register.get();
	}
	
	public void deleteAlarm(Long idAlarm) throws AlarmNotFoundException  {
		if(!alarmRepository.existsById(idAlarm)) 
			throw new AlarmNotFoundException(idAlarm);
		
		alarmRepository.deleteById(idAlarm);
	}
	
	public List<Alarm> findFilter(Alarm filter){
		Example<Alarm> example = Example.of(filter, ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));
		return alarmRepository.findAll(example);
	}
	
	

}
