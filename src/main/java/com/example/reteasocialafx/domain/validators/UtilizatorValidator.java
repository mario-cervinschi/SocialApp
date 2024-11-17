package com.example.reteasocialafx.domain.validators;


import com.example.reteasocialafx.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getFirstName().isEmpty())
            throw new ValidationException("Nume utilizator gol");
        if(entity.getLastName().isEmpty())
            throw new ValidationException("Nume utilizator gol");
    }
}
