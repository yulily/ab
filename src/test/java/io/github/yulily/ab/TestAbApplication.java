package io.github.yulily.ab;

import org.springframework.boot.SpringApplication;

public class TestAbApplication {

	public static void main(String[] args) {
		SpringApplication.from(AbApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
