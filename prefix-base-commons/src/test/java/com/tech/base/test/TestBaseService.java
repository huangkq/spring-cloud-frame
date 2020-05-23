package com.tech.base.test;

import org.junit.Test;

//@RunWith(SpringRunner.class)
public class TestBaseService {
    
    @Test
    public void testIsNull() {
        try {
            BaseServiceImpl baseServiceImpl = new BaseServiceImpl();
            baseServiceImpl.testIsNull();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
