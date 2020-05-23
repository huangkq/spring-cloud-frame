package com.tech.base.test;

import static org.junit.Assert.assertTrue;

import com.tech.base.service.BaseService;
import com.tech.base.utils.ValidateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseServiceImpl extends BaseService{

    public void testIsNull(){
        int a=0;
        logger.info("testIsNull...");
        assertTrue(this.isNotEmpty(Arrays.asList("")));
        Integer[] as = new Integer[] {0, 1, 2};
        assertTrue(ValidateUtil.isNotEmpty(as));
        assertTrue(this.isEmpty(a));
        assertTrue(this.isEmpty(null));
        assertTrue(this.isEmpty(new String(" ")));
        assertTrue(this.isEmpty(new Long(0L)));
        assertTrue(this.isEmpty(new Integer(0)));
        assertTrue(this.isEmpty(new Byte((byte) 0)));
        assertTrue(this.isEmpty(new Double(0)));
        assertTrue(this.isEmpty(new ArrayList<>()));
        assertTrue(this.isEmpty(new BigDecimal("0.00")));
    }
}
