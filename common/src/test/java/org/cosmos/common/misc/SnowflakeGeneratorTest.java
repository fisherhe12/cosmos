package org.cosmos.common.misc;


import org.junit.Test;

public class SnowflakeGeneratorTest {

    @Test
    public void generate(){
        SnowflakeGenerator snowflakeGenerator=new SnowflakeGenerator(0,0);
        System.out.println(snowflakeGenerator.nextId());
    }
}
