package com.colibrihub.CommerceCore.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta del proceso de sincronización de productos")
public class SyncResponse {
    @Schema(description = "Cantidad total de productos sincronizados", example = "25")
    private int totalSynced;
    @Schema(description = "Mensaje descriptivo del resultado", example = "Sincronización completada de forma correcta")
    private String message;
}
