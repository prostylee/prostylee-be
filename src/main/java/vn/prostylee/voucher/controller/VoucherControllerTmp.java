package vn.prostylee.voucher.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.voucher.dto.response.VoucherResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/voucher")
public class VoucherControllerTmp {

    @GetMapping("/userLogin")
    public Page<VoucherResponse> getVoucherByUserLogin(MasterDataFilter filter){
        List<VoucherResponse> voucherResponseList = new ArrayList<>();
        for (int i = 0; i<10; i++){
            VoucherResponse item = new VoucherResponse();
            item.setId(1L + i);
            item.setName("Giảm 500k cho đơn hàng từ 5 triệu trở lên");
            item.setKey("500k5" + i);
            item.setLogo("https://d1fq4uh0wyvt14.cloudfront.net/fit-in/90x120/public/ec72c651-d66a-4bfb-950c-f6b8e2132f30/DA571D52-3333-4BEF-BA32-3830B6EF5617.jpg");
            item.setVoucherOwner("Pull&Bear" + i);
            voucherResponseList.add(item);
        }
        return new PageImpl<>(voucherResponseList);
    }

}
