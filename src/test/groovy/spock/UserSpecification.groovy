package spock

import cz.upce.music.entity.Users
import cz.upce.music.repository.UsersRepository
import cz.upce.music.service.interfaces.UserService
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import spock.lang.Specification

@SpringBootTest
@DataJpaTest
class UserSpecification extends Specification {

    @Rollback
    def "mock save user"() {
        given:
        def user = new Users(username: "user", password: "password")
        UserService userService = Mock()
        UsersRepository userRepository = Mock()

        userRepository.findUserByUsername(_ as String) >> { args ->
            String userName = args[0]
            Users entity = new Users()
            entity.setUsername(userName)
            entity.setId(1L)
            return entity
        }

        when:
        userService.saveUser(user);

        then:
        1 * userService.saveUser(user)
        userRepository.findUserByUsername(user.username) != null
    }

}
