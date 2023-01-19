package com.pizzapp.service;

import com.pizzapp.data.Client;
import com.pizzapp.data.Ingredient;
import com.pizzapp.data.Pizza;
import com.pizzapp.data.ShoppingCart;
import com.pizzapp.dto.ClientDTO;
import com.pizzapp.repository.ClientRepository;
import com.pizzapp.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    public Optional<Client> getClientById(long id) {
        Optional<Client> client = Optional.empty();
        if (id > 0) {
            client = clientRepository.findById(id);
        }
        return client;
    }

    public ClientDTO getClientByUsernameDTO(String username) {
        Optional<Client> client = Optional.empty();
        if (!Objects.equals(username, "")) {
            client = clientRepository.findByUsername(username);
        }
        return ClientDTO.convertClientToDTO(client);
    }

    public boolean saveClient(Client client) {
        boolean saveSuccesfull = false;
        if (client != null
                && client.getUsername() != null
                && clientRepository.findByUsername(client.getUsername()).isEmpty()) {
            clientRepository.save(client);
            saveSuccesfull = true;
        }
        return saveSuccesfull;
    }

    public boolean saveClient(String username) {
        boolean saveSuccesfull = false;
        if (!Objects.equals(username, "") && clientRepository.findByUsername(username).isEmpty()) {
            Client client = Client.builder()
                    .username(username)
                    .money(0).build();
            clientRepository.save(client);
            saveSuccesfull = true;
        }
        return saveSuccesfull;
    }

    public boolean deleteClientById(long id) {
        boolean deleted = false;
        Optional<Client> client = getClientById(id);
        if (client.isPresent()) {
            clientRepository.deleteById(client.get().getId());
            deleted = true;
        }
        return deleted;
    }

    public boolean saveShoppingCart(ShoppingCart shoppingCart) {
        boolean saveSuccesfull = false;
        if (shoppingCart != null) {
            shoppingCartRepository.save(shoppingCart);
            saveSuccesfull = true;
        }
        return saveSuccesfull;
    }

    public Optional<ShoppingCart> getShoopingCartById(long id) {
        Optional<ShoppingCart> shoppingCart = Optional.empty();
        if (id > 0) {
            shoppingCart = shoppingCartRepository.findById(id);
        }
        return shoppingCart;
    }

    public Optional<Client> getClientByShoopingCartId(long id) {
        Optional<ShoppingCart> shoppingCart = getShoopingCartById(id);
        Optional<Client> client = Optional.empty();
        if (shoppingCart.isPresent()) {
            client = Optional.ofNullable(shoppingCart.get().getClient());
        }
        return client;
    }

    public Optional<List<ShoppingCart>> getShoppingCartsByClientId(long id) {
        Optional<Client> client = getClientById(id);
        Optional<List<ShoppingCart>> shoppingCartList = Optional.empty();
        if (client.isPresent()) {
            shoppingCartList = Optional.ofNullable(client.get().getShoppingCarts());
        }
        return shoppingCartList;
    }

    private boolean updateState(long shoppingCartId) {
        boolean updated = false;
        assert shoppingCartId > 0;
        Optional<ShoppingCart> shoppingCart = getShoopingCartById(shoppingCartId);
        if (shoppingCart.isPresent()) {
            shoppingCart.get().setBuyed(true);
            if (saveShoppingCart(shoppingCart.get())) {
                updated = true;
            }
        }
        return updated;
    }

    public boolean updateMoney(long clientId, float money) {
        boolean updated = false;
        Optional<Client> client = getClientById(clientId);
        if (client.isPresent()) {
            client.get().setMoney(money);
            if (saveClient(client.get())) {
                updated = true;
            }
        }
        return updated;
    }

    public boolean buy(long shoopingCartId) {
        boolean buyed = false;
        Optional<ShoppingCart> shoppingCart = getShoopingCartById(shoopingCartId);
        Optional<Client> client = getClientByShoopingCartId(shoopingCartId);
        if (shoppingCart.isPresent()
                && client.isPresent()
                && client.get().getMoney() > shoppingCart.get().getTotalPrice()
                && updateMoney(client.get().getId(), client.get().getMoney() - shoppingCart.get().getTotalPrice())
                && updateState(shoopingCartId)) {
            buyed = true;
        }
        return buyed;
    }

    public boolean addPizzaToShoppingCart(long shoopingCartId, Pizza pizza) {
        boolean added = false;
        if (shoopingCartId > 0) {
            Optional<ShoppingCart> shoppingCart = getShoopingCartById(shoopingCartId);
            if (shoppingCart.isPresent()
                    && shoppingCart.get().addPizza(pizza)
                    && saveShoppingCart(shoppingCart.get())) {
                added = true;
            }
        }
        return added;
    }

    public boolean removePizzaToShoppingCart(long shoopingCartId, Pizza pizza) {
        boolean removed = false;
        if (shoopingCartId > 0) {
            Optional<ShoppingCart> shoppingCart = getShoopingCartById(shoopingCartId);
            if (shoppingCart.isPresent()
                    && shoppingCart.get().removePizza(pizza)
                    && saveShoppingCart(shoppingCart.get())) {
                removed = true;
            }
        }
        return removed;
    }

    public boolean addIngredient(final long pizzaId, Ingredient ingredient, long shoppingCartId) {
        final boolean[] added = {false};
        if (pizzaId > 0 && shoppingCartId > 0 && ingredient != null) {
            Optional<ShoppingCart> optionalShoppingCart = this.getShoopingCartById(shoppingCartId);
            optionalShoppingCart.ifPresent(shoppingCart -> {
                shoppingCart.getPizzas().forEach(p -> {
                    if (pizzaId == p.getId()) {
                        p.addIngredient(ingredient);
                        added[0] = true;
                    }
                });
                saveShoppingCart(shoppingCart);
            });
        }
        return added[0];
    }

    public boolean removeIngredient(final long pizzaId, Ingredient ingredient, long shoppingCartId) {
        final boolean[] removed = {false};
        if (pizzaId > 0 && shoppingCartId > 0 && ingredient != null) {
            Optional<ShoppingCart> optionalShoppingCart = this.getShoopingCartById(shoppingCartId);
            optionalShoppingCart.ifPresent(shoppingCart -> {
                shoppingCart.getPizzas().forEach(p -> {
                    if (pizzaId == p.getId()) {
                        p.removeIngredient(ingredient);
                        removed[0] = true;
                    }
                });
                saveShoppingCart(shoppingCart);
            });
        }
        return removed[0];
    }
}