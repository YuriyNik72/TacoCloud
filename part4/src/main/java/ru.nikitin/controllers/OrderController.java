package ru.nikitin.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import ru.nikitin.entity.TacoOrder;
import ru.nikitin.repository.OrderRepository;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping ("/orders") //Все запросы будут начинаться с /orders
@SessionAttributes("tacoOrder")
public class OrderController {

    private OrderRepository orderRepo;
    public OrderController(@Qualifier("orderRepository") OrderRepository orderRepo){
        this.orderRepo = orderRepo;
    }
    @GetMapping("/current") //Будет выполнен запрос GET по адрессу /orders/current
    public String oderForm(){
        return "orderForm";
    }
    @PostMapping  //Будет выполнен запрос POST по адрессу /orders
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus){
        /*
        С проверкой на валидность, если данные будут введены не верно- обработается ошибка и назад
        вернется форма для корректного заполнения
         */
        if(errors.hasErrors()){
            return "orderForm";
        }
        log.info("Order submitted: { }", order);
        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
