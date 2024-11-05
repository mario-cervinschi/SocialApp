package repository;

import domain.Utilizator;
import domain.validators.Validator;

import java.util.List;

public class UtilizatorRepository extends AbstractFileRepository<Long, Utilizator>{
    public UtilizatorRepository(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    public Utilizator extractEntity(List<String> data) {
        Utilizator utilizator = new Utilizator(data.get(1), data.get(2));
        utilizator.setId(Long.parseLong(data.get(0)));
        return utilizator;
    }

    @Override
    public String createEntityAsString(Utilizator entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();
    }
}
