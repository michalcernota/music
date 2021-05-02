package spock

import cz.upce.music.entity.User
import cz.upce.music.repository.UserRepository
import cz.upce.music.service.UserService
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
        def user = new User(username: "user", password: "password")
        UserService userService = Mock()
        UserRepository userRepository = Mock()

        userRepository.findUserByUsername(_ as String) >> { args ->
            String userName = args[0]
            User entity = new User()
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
