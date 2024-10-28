import domain.validators.PrietenieValidator;
import domain.validators.UtilizatorValidator;
import repository.PrietenieRepository;
import repository.UtilizatorRepository;
import service.SocialService;
import ui.Console;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        UtilizatorRepository repoUser = new UtilizatorRepository(new UtilizatorValidator(), "C:\\UBB\\semestrul 3\\MAP\\ReteaSocializare\\src\\data\\users.csv");
        PrietenieRepository repoFriend = new PrietenieRepository(new PrietenieValidator(repoUser), "C:\\UBB\\semestrul 3\\MAP\\ReteaSocializare\\src\\data\\prietenii.csv", repoUser);

        SocialService srv = new SocialService(repoUser, repoFriend);
        Console console = new Console(srv);

        console.run();
    }
}