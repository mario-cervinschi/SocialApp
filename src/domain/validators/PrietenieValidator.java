package domain.validators;

import domain.Prietenie;
import domain.Utilizator;
import repository.InMemoryRepository;

public class PrietenieValidator implements Validator<Prietenie> {

    private final InMemoryRepository<Long, Utilizator> repository;

    public PrietenieValidator(InMemoryRepository<Long, Utilizator> repository) {
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
