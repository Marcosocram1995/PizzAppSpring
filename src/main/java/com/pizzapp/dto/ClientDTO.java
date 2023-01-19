package com.pizzapp.dto;

import com.pizzapp.data.Client;
import com.pizzapp.data.Pizza;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.Optional;

@Data
public class ClientDTO {
    private String username;
    private String money;

    public static ClientDTO convertClientToDTO(Optional<Client> optionalClient) {
        ClientDTO clientDTO = new ClientDTO();
        if (optionalClient.isPresent()) {
            clientDTO.username = optionalClient.get().getUsername();
            clientDTO.money = String.valueOf(optionalClient.get().getMoney());
        }
        return clientDTO;
    }
}