package com.ycx.test;


import com.ycx.net.rpc.client.KtRpcClient;
import com.ycx.net.test.RpcTestService;
import com.ycx.pojo.PatRegister;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.List;

public class TestRpcClient extends AbstractJavaSamplerClient {

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSamplerData("RPC Request Data");
        sampleResult.setSampleLabel("RPC Test");
        sampleResult.sampleStart();

        try {
            KtRpcClient client = new KtRpcClient("127.0.0.1:4399");
            RpcTestService service = client.createService(RpcTestService.class, "1.0");
            List<PatRegister> result = service.getPatRegisterList(1);
            sampleResult.sampleEnd();
            sampleResult.setSuccessful(true);
            sampleResult.setResponseData(result.toString(), "UTF-8");
            sampleResult.setResponseCodeOK();
        } catch (Exception e) {
            sampleResult.setResponseCode("500");
            sampleResult.setResponseMessage(e.getMessage());
            sampleResult.setSuccessful(false);
        }
        return sampleResult;
    }
}
