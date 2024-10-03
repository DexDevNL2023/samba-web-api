package com.teleo.manager.parametre.services.impl;

import com.teleo.manager.generic.exceptions.InternalException;
import com.teleo.manager.generic.exceptions.RessourceNotFoundException;
import com.teleo.manager.generic.logging.LogExecution;
import com.teleo.manager.generic.service.ImageService;
import com.teleo.manager.generic.service.impl.ServiceGenericImpl;
import com.teleo.manager.parametre.dto.reponse.CompanyResponse;
import com.teleo.manager.parametre.dto.request.CompanyRequest;
import com.teleo.manager.parametre.entities.Company;
import com.teleo.manager.parametre.mapper.CompanyMapper;
import com.teleo.manager.parametre.repositories.CompanyRepository;
import com.teleo.manager.parametre.services.CompanyService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyServiceImpl extends ServiceGenericImpl<CompanyRequest, CompanyResponse, Company> implements CompanyService {

    private final CompanyRepository repository;
    private final CompanyMapper mapper;
    private final ImageService imageService;

    public CompanyServiceImpl(CompanyRepository repository, CompanyMapper mapper, ImageService imageService) {
        super(Company.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.imageService = imageService;
    }

    @Transactional
    @LogExecution
    @Override
    public CompanyResponse save(CompanyRequest dto) throws RessourceNotFoundException {
        try {
            Company company = mapper.toEntity(dto);

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getLogo());
            // On construit l'url absolue du fichier
            company.setLogo(fileUrl);

            company = repository.save(company);
            return getOne(company);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public CompanyResponse update(CompanyRequest dto, Long id) throws RessourceNotFoundException {
        try {
            Company company = getById(id);

            // Comparer les données du DTO pour éviter la duplication
            if (company.equalsToDto(dto)) {
                throw new RessourceNotFoundException("La ressource company avec les données suivantes : " + dto.toString() + " existe déjà");
            }

            // Mise à jour des informations de la réclamation
            company.update(mapper.toEntity(dto));

            // Decode Base64 and save the file
            String fileUrl = imageService.saveBase64File(dto.getLogo());
            // On construit l'url absolue du fichier
            company.setLogo(fileUrl);

            company = repository.save(company);
            return getOne(company);
        } catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Transactional
    @LogExecution
    @Override
    public CompanyResponse findFirstCompany() {
        return mapper.toDto(repository.findFirstByOrderByIdAsc());
    }
}
