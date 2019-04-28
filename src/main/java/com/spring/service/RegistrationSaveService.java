package com.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.Exception.EmailExistsException;
import com.spring.model.RegistrationFlow;
import com.spring.model.VerificationToken;
import com.spring.persistence.RegistrationFlowRepository;
import com.spring.persistence.VerificationTokenRepository;

@Service
@Transactional
public class RegistrationSaveService implements InterfRegistrationSaveService {
	
	@Autowired
	private RegistrationFlowRepository registrationFlowRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public RegistrationFlow save(RegistrationFlow registrationFlow) throws EmailExistsException {
		if(emailExist(registrationFlow.getEmail())) {
			throw new EmailExistsException("There is an account with that Email address  "+registrationFlow.getEmail());
		}
		registrationFlow.setPassword(passwordEncoder.encode(registrationFlow.getPassword()));
		return registrationFlowRepository.save(registrationFlow);
	}

	private boolean emailExist(String email) {
		final RegistrationFlow registrationFlow = registrationFlowRepository.findByEmail(email);
		return registrationFlow != null;
	}

	@Override
	public void createVerificationToken(final RegistrationFlow registrationFlow, String token) {
		final VerificationToken myToken = new VerificationToken(token,registrationFlow);
		verificationTokenRepository.save(myToken);
		
	}

	@Override
	public VerificationToken getVerificationToken(String token) {
		return verificationTokenRepository.findByToken(token);
	}

	@Override
	public void saveRegisteredUser(RegistrationFlow registrationFlow) {
		registrationFlowRepository.save(registrationFlow);
	}

}
