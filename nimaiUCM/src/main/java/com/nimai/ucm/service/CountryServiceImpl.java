package com.nimai.ucm.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nimai.ucm.bean.CountryResponse;
import com.nimai.ucm.bean.StateResponce;
import com.nimai.ucm.entity.Countries;
import com.nimai.ucm.entity.NimaiLookupCountries;

import com.nimai.ucm.repository.NimaiLookupCountriesRepository;

@Service
public class CountryServiceImpl implements CountryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterServiceImpl.class);

	

	@Autowired
	NimaiLookupCountriesRepository nimaiLookupCountriesRepository;

	// Working oNe
	public List<?> countryData() {
		List<NimaiLookupCountries> list = nimaiLookupCountriesRepository.findAll();

		List<CountryResponse> countryResponse = list.stream().map(request -> {
			CountryResponse countryData = new CountryResponse();
			countryData.setCountryId(request.getCountryId());
			countryData.setCountryName(request.getCountryName());
			countryData.setPhoneCode(request.getPhoneCode());
			countryData.setCurrency(request.getCurrency());
			List<StateResponce> stateList = request.getNimaiLookupStatesList().stream().map(stateReq -> {
				StateResponce stateResponse = new StateResponce();
				stateResponse.setStateId(stateReq.getStateId());
				stateResponse.setStateName(stateReq.getStateName());
				return stateResponse;
			}).collect(Collectors.toList());
			countryData.setStateResponce(stateList);
			return countryData;
		}).collect(Collectors.toList());

		return countryResponse;
	}
}
