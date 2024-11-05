import domain.validators.PrietenieValidator;
import domain.validators.UtilizatorValidator;
import repository.database.FriendshipDB;
import repository.database.UserDB;
import service.SocialService;
import ui.Console;

public class Main {
    public static void main(String[] args) {

        UserDB repoUser = new UserDB(new UtilizatorValidator());
        FriendshipDB repoFriend = new FriendshipDB(new PrietenieValidator(repoUser));

        SocialService srv = new SocialService(repoUser, repoFriend);
        Console console = new Console(srv);

        console.run();
    }
}