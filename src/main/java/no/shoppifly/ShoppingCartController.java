package no.shoppifly;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

@RestController()
public class ShoppingCartController {


    private Timer productTimer;
    private MeterRegistry meterRegistry;

    private Map<String, Cart> theShoppingCart = new HashMap();

    @Autowired
    private CartService cartService;

    @Autowired
    public ShoppingCartController(MeterRegistry meterRegistry) {

        this.meterRegistry = meterRegistry;
    }

    public ShoppingCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(path = "/cart/{id}")
    public Cart getCart(@PathVariable String id) {
        return cartService.getCart(id);
    }

    /**
     * Checks out a shopping cart. Removes the cart, and returns an order ID
     *
     * @return an order ID
     */
    @Timed
    @PostMapping(path = "/cart/checkout")
    public String checkout(@RequestBody Cart cart) {
        long startTime = System.currentTimeMillis();

        meterRegistry.counter("checkout").increment();
        meterRegistry.timer("checkout_latency").record(Duration.ofMillis(System.currentTimeMillis() - startTime));
        return cartService.checkout(cart);
    }

    /**
     * Updates a shopping cart, replacing it's contents if it already exists. If no cart exists (id is null)
     * a new cart is created.
     *
     * @return the updated cart
     */
    @PostMapping(path = "/cart")
    public Cart updateCart(@RequestBody Cart cart) {
        meterRegistry.counter("update_cart").increment();
        return cartService.update(cart);
    }

    /**
     * return all cart IDs
     *
     * @return
     */
    @GetMapping(path = "/carts")
    public List<String> getAllCarts() {
        meterRegistry.counter("carts").increment();
        return cartService.getAllsCarts();
    }


}