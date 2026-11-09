package com.pwyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class PwylApplication {

	public static void main(String[] args) {
		SpringApplication.run(PwylApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  陪玩语聊项目启动成功   ლ(´ڡ`ლ)ﾞ  \n"+
							"       _    \n" +
							"      | |   \n" +
							"  ___ | | __\n" +
							" / _ \\| |/ /\n" +
							"| (_) |   < \n" +
							" \\___/|_|\\_\\");

	}

}