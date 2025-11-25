package com.ys.system;

import com.ys.common.security.annotation.EnableCustomConfig;
import com.ys.common.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *  System Module
 *
 * @author ys
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {"com.ys.system","com.ys.utils"})
public class YsSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(YsSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ   System Module Startup SUCCESS   ლ(´ڡ`ლ)ﾞ  \n" +
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
