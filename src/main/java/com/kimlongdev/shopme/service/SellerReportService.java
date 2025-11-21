package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.modal.Seller;
import com.kimlongdev.shopme.modal.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport( SellerReport sellerReport);

}
