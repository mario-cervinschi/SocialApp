package repository.file;

import domain.Prietenie;
import domain.Utilizator;
import domain.validators.Validator;
import repository.database.UserDB;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PrietenieRepository extends AbstractFileRepository<Long, Prietenie>{

    private final UserDB repoUser;

    public PrietenieRepository(Validator<Prietenie> validator, String fileName, UserDB repoUser) {
        super(validator);
        this.filename = fileName;
        this.repoUser = repoUser;
        super.loadData();
    }

    @Override
    public Prietenie extractEntity(List<String> data) {
        Optional<Utilizator> u1 = repoUser.findOne(Long.parseLong(data.get(1)));
        Optional<Utilizator> u2 = repoUser.findOne(Long.parseLong(data.get(2)));

        Prietenie friendship = new Prietenie(Long.parseLong(data.get(1)), Long.parseLong(data.get(2)));
        friendship.setId(Long.parseLong(data.get(0)));
        friendship.setDate(LocalDateTime.parse(data.get(3)));

        if (u1.orElse(null) != null) {
            u1.orElse(null).addFriend(u2.orElse(null));
        }

        if(u2.orElse(null) != null) {
            u2.orElse(null).addFriend(u1.orElse(null));
        }

        return friendship;
    }

    @Override
    public String createEntityAsString(Prietenie entity) {
        return entity.getId() + ";" + entity.getIdUser1() + ";" + entity.getIdUser2() + ";" + entity.getDate();
    }
}
