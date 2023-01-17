package ru.nikitin.repository;

import org.springframework.asm.Type;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nikitin.data.IngredientRef;
import ru.nikitin.entity.Taco;
import ru.nikitin.entity.TacoOrder;

import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository{
    private JdbcOperations jdbcOperations;
    
    public JdbcOrderRepository(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }
    
    @Override
    @Transactional
    public TacoOrder save(TacoOrder order) {
        /*
        создание объекта PreparedStatementCreatorFactory, описывающего запрос insert вместе с типами
        параметров запроса. Поскольку позднее нам понадобится идентификатор сохраненного заказа, мы
        также должны вызвать метод setReturnGeneratedKeys(true) этого объекта.
         */
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory("insert into Taco_Order"
             +"(delivery_name, delivery_street, delivery_city,"
             + "delivery_state, delivery_zip, cc_number, cc_expiration, cc_cvv, placed_at)"
             +"values(?,?,?,?,?,?,?,?,?)",
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP
        );
        pscf.setReturnGeneratedKeys(true);
        
        order.setPlacedAt(new Date());
        /*
        После создания PreparedStatementCreatorFactory мы используем его для создания объекта PreparedStatementCreator,
         передавая значения из объекта TacoOrder, которые требуется сохранить. Последний аргумент в вызове
         PreparedStatementCreator – это дата создания заказа, которую также нужно будет установить в самом объекте
         TacoOrder, чтобы возвращаемый экземпляр TacoOrder содержал эту информацию.
         */
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        order.getDeliveryName(),
                        order.getDeliveryStreet(),
                        order.getDeliveryCity(),
                        order.getDeliveryState(),
                        order.getDeliveryZip(),
                        order.getCcNumber(),
                        order.getCcExpiration(),
                        order.getCcCVV(),
                        order.getPlacedAt()
                )
        );

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        /*
        После создания PreparedStatementCreator можно фактически сохранить заказ, вызвав метод JdbcTemplate.update(),
        передав ему PreparedStatementCreator и GeneratedKeyHolder. После сохранения заказа в GeneratedKeyHolder будет
        храниться значение поля id, назначенное базой данных, которое затем следует скопировать в свойство id объекта TacoOrder.
         */
        jdbcOperations.update(psc, keyHolder);
        long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);
        
        List<Taco> tacos = order.getTacos();
        int i = 0;
        for (Taco taco : tacos){
            saveTaco(orderId, i++, taco);
        }
        return order;
    }

    /*
     после сохранения заказа, необходимо также сохранить объекты Taco, связанные с заказом. Это можно сделать вызовом
     метода saveTaco() для каждого Taco в заказе. Метод saveTaco() очень похож на метод save()
     */
    private long saveTaco(long orderId, int orderKey, Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
                "insert into Taco"
                + "(name, created_at, taco_order, taco_order_key)"
                +"values (?,?,?,?)",
                Types.VARCHAR, Types.TIMESTAMP, Type.LONG, Type.LONG
        );
        pscf.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        taco.getCreatedAt(),
                        orderId,
                        orderKey
                )
        );
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(psc, keyHolder);
        long tacoId = keyHolder.getKey().longValue();
        taco.setId(tacoId);

        saveIngredientRefs(tacoId,taco.getIngredients());
        return tacoId;
    }



    private void saveIngredientRefs(long tacoId, List<IngredientRef> ingredientRefs) {
        int key = 0;
        for (IngredientRef ingredientRef:ingredientRefs){
            jdbcOperations.update(
                    "insert into Ingredient_Ref (ingredient, taco, taco_key)"
                    + "values (?,?,?)",
                    ingredientRef.getIngredient(), tacoId, key++
            );
        }
    }
}
