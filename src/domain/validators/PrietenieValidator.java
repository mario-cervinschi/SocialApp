package domain.validators;

import domain.Prietenie;
import repository.database.UserDB;

public class PrietenieValidator implements Validator<Prietenie> {

    private final UserDB repository;

    public PrietenieValidator(UserDB repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if(entity.getIdUser1() == null || entity.getIdUser2() == null) {
            throw new ValidationException("IdUser1 or IdUser2 cannot be null");
        }
        if(repository.findOne(entity.getIdUser1()).isEmpty() || repository.findOne(entity.getIdUser2()).isEmpty()) {
            throw new ValidationException("IdUser1 or IdUser2 does not exist");
        }
    }
}
