package com.ys.hr.sign.factory;


import com.ys.hr.sign.entiry.SignType;
import com.ys.hr.sign.strategy.PandaDocStrategy;
import com.ys.hr.sign.strategy.SignApiStrategy;
import org.springframework.stereotype.Component;

/**
 * Strategy Implementation Factory Class
 */
@Component
public class SignApiStrategyFactory {

    /**
     * Create the corresponding implementation class according to the Type.
     * @param signType
     * @return
     * @param <T>
     * @param <R>
     */
    public <T, R> SignApiStrategy<T, R> createStrategy(SignType signType) {
        if (signType == null) {
            throw new RuntimeException("Platform type cannot be empty");
        }
        switch (signType) {
            case PANDA_DOC:
                return (SignApiStrategy<T, R>) new PandaDocStrategy();
            default:
                throw new RuntimeException("Platform type does not exist");
        }
    }
}
