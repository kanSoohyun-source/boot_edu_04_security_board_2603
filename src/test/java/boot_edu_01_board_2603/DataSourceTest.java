package boot_edu_01_board_2603;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@SpringBootTest
public class DataSourceTest {
    @Autowired
    private DataSource dataSource; // 아무 문제가 없다는 것은 DataSource가 자동으로 빈 주입이 되었다는 것

    @Test
    public void testConnection() throws SQLException {
        @Cleanup Connection connection = dataSource.getConnection();
        log.info(connection);
    }

}