package com.frankmoley.boot.essentials.initialbootapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
class InitialBootAppController{
@RequestMapping(method = RequestMethod.GET, path = "/listVisualiztion")
public List<Owners> getAllOwners() {
    logger.info("Entered into Controller Layer");
    List<Owners> results = ownersRepository.findAll();
    logger.info("Number of Records Fetched:" + results.size());
    return results;
}
}
