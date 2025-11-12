package com.ys.hr;

import com.ys.common.security.annotation.EnableCustomConfig;
import com.ys.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * System Module
 *
 * @author ruoyi
 */
@EnableCustomConfig
@EnableRyFeignClients
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.ys.hr","com.ys.utils","com.ys.sync"})
public class YsHrApplication {
    public static void main(String[] args) {
        SpringApplication.run(YsHrApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ   System Module SUCCESS   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}
