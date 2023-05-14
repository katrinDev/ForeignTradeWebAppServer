package com.company.foreignTradeOperationsWebApp.controllers;

import com.company.foreignTradeOperationsWebApp.models.CompanyEntity;
import com.company.foreignTradeOperationsWebApp.models.ItemEntity;
import com.company.foreignTradeOperationsWebApp.models.TradeTypeEntity;
import com.company.foreignTradeOperationsWebApp.payloads.response.MessageResponse;
import com.company.foreignTradeOperationsWebApp.repositories.CompanyRepository;
import com.company.foreignTradeOperationsWebApp.repositories.TrendTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TrendTypeRepository trendTypeRepository;

    @GetMapping("")
    public ResponseEntity<List<CompanyEntity>> getAllCompanies(){
        List<CompanyEntity> companies = companyRepository.findAll();

        if (companies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity<CompanyEntity> deleteCompany(@PathVariable("id") Long id){
        CompanyEntity company = companyRepository.findById(id).orElse(null);

        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        companyRepository.deleteById(id);
        System.out.println("Company to be deleted: " + company);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> addCompany(@RequestBody CompanyEntity company) {
        if (company == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        System.out.println("New company: " + company);

        if (companyRepository.existsByCompanyName(company.getCompanyName())) {
            return new ResponseEntity<>(new MessageResponse("Ошибка: Компания с таким названием уже была зарегистрирована!"), HttpStatus.BAD_REQUEST);
        }

        try{
            TradeTypeEntity tradeTypeEntity = trendTypeRepository.findByTradeTypeName(company.getTradeType().getTradeTypeName());
            CompanyEntity newCompany = new CompanyEntity(company.getCompanyName(), company.getCountry(),
                    company.getCheckingAccount(), company.getCompanyEmail(), tradeTypeEntity);
            companyRepository.save(newCompany);
            return ResponseEntity.ok(newCompany);

        } catch(Exception e) {
            return new ResponseEntity<>(new MessageResponse("Ошибка: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping (value = "")
    public ResponseEntity<?> updateCompany(@RequestBody CompanyEntity company) {

        if (company == null){
            return new ResponseEntity<>(new MessageResponse("Некорректное тело запроса!"), HttpStatus.BAD_REQUEST);
        }

        try{
            if(companyRepository.findById(company.getCompanyId()).orElse(null) == null){
                return new ResponseEntity<>(new MessageResponse("Данного компании нет в системе!"), HttpStatus.NOT_FOUND);
            }

            if (companyRepository.existsByCompanyName(company.getCompanyName()) &&
                    !Objects.equals((companyRepository.findByCompanyName(company.getCompanyName())).getCompanyId(), company.getCompanyId())) {
                return new ResponseEntity<>(new MessageResponse("Ошибка: Компания с таким названием уже была зарегистрирована!"), HttpStatus.BAD_REQUEST);
            }

            TradeTypeEntity tradeTypeEntity = trendTypeRepository.findByTradeTypeName(company.getTradeType().getTradeTypeName());
            CompanyEntity updatedCompany = new CompanyEntity(company.getCompanyId(), company.getCompanyName(), company.getCountry(),
                    company.getCheckingAccount(), company.getCompanyEmail(), tradeTypeEntity);
            companyRepository.save(updatedCompany);

            System.out.println("Item was updated: " + updatedCompany);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new MessageResponse("Не удалось изменить данные компании!"), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(company, HttpStatus.OK);
    }
}
