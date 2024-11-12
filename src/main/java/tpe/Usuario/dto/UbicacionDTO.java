package tpe.Usuario.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UbicacionDTO {
    private Long longitud;
    private Long latitud;

    public UbicacionDTO(){
    }
    public UbicacionDTO(Long longitud, Long latitud){
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public Long getLongitud() {
        return longitud;
    }

    public void setLongitud(Long longitud) {
        this.longitud = longitud;
    }

    public Long getLatitud() {
        return latitud;
    }

    public void setLatitud(Long latitud) {
        this.latitud = latitud;
    }
}
