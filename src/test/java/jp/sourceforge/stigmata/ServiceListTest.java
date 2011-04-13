package jp.sourceforge.stigmata;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Haruaki Tamada
 */
public class ServiceListTest{
    private BirthmarkEnvironment env;

    @Before
    public void setup() throws Exception{
        env = Stigmata.getInstance().createEnvironment();
    }

    @Test
    public void testServiceList(){
        assertNotNull(env.getService("cvfv"));
        assertNotNull(env.getService("fmc"));
        assertNotNull(env.getService("fuc"));
        assertNotNull(env.getService("is"));
        assertNotNull(env.getService("smc"));
        assertNotNull(env.getService("uc"));
        assertNotNull(env.getService("cvfv_dp"));
        assertNotNull(env.getService("cvfv_ed"));
        assertNotNull(env.getService("is_dp"));
        assertNotNull(env.getService("is_ed"));
        assertNotNull(env.getService("smc_dp"));
        assertNotNull(env.getService("smc_ed"));
        assertNotNull(env.getService("uc_dp"));
        assertNotNull(env.getService("uc_ed"));
        assertNotNull(env.getService("uc_seq"));
    }
}
