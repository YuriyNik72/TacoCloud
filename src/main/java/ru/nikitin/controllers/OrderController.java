package ru.nikitin.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import ru.nikitin.TacoOrder;

@Slf4j
@Controller
@RequestMapping ("/orders") //Все запросы будут начинаться с /orders
@SessionAttributes("tacoOrder")
public class OrderController {
    @GetMapping("/current") //Будет выполнен запрос GET по адрессу /orders/current
    public String oderForm(){
        return "orderForm";
    }
    @PostMapping  //Будет выполнен запрос POST по адрессу /orders
    public String processOrder(TacoOrder order, SessionStatus sessionStatus){
        log.info("Order submitted: { }", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
