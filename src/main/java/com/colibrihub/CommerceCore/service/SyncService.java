package com.colibrihub.CommerceCore.service;

import com.colibrihub.CommerceCore.dto.Response.SyncResponse;
import com.colibrihub.CommerceCore.dto.Response.WooProductResponse;

import java.util.List;

public interface SyncService {
    SyncResponse syncProducts();
}
