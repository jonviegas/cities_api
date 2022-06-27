package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.docs.AbstractControllerDocs;
import com.jonservices.citiesapi.exceptions.ResourceNotFoundException;
import com.jonservices.citiesapi.hateoas.HateoasImpl;
import com.jonservices.citiesapi.repositories.AbstractRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

import static com.jonservices.citiesapi.utils.GenericsUtils.getParameterTypeName;

@AllArgsConstructor
@CrossOrigin(maxAge = 3600)
public abstract class AbstractController<T extends RepresentationModel<T>> implements AbstractControllerDocs<T> {
    protected final AbstractRepository<T> repository;

    @GetMapping
    public Page<T> findAll(Pageable page) {
        final Page<T> regionPage = repository.findAll(page);
        regionPage.forEach(HateoasImpl::addLinkToItself);
        return regionPage;
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable Long id) {
        final T region = verifyIfExists(id);
        HateoasImpl.addLinkToAll(region);
        return region;
    }

    @GetMapping("/search/{name}")
    public Page<T> findByName(@PathVariable String name, Pageable page) {
        final Page<T> regionPage = verifyIfExists(name, page);
        regionPage.forEach(HateoasImpl::addLinkToItself);
        return regionPage;
    }

    protected T verifyIfExists(Long id) {
        final Optional<T> region = repository.findById(id);
        if (region.isEmpty())
            handleError("id", id);
        return region.get();
    }

    protected Page<T> verifyIfExists(String name, Pageable page) {
        final Page<T> regionPage = repository.findByNameContainingIgnoreCase(name, page);
        if (regionPage.isEmpty())
            handleError("name", name);
        return regionPage;
    }

    protected void handleError(String attribute, Object value) {
        throw new ResourceNotFoundException(getParameterTypeName(this), attribute, value);
    }

}
