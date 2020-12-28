package sui.cloud.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TokenFilter implements GlobalFilter,Ordered{

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -100;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		if(exchange.getRequest().getURI().getPath().equals("/user-service/login")) {//白名单
			return chain.filter(exchange);
		}
		try {
            JWT.require(Algorithm.HMAC256("Sui")).build().verify(exchange.getRequest().getHeaders().get("token").get(0));
            return chain.filter(exchange);
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        	return responseText(exchange.getResponse());
		}
	}
	public Mono<Void> responseText(ServerHttpResponse response) {
		response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		DataBuffer dataBuffer = response.bufferFactory().wrap("你没有足够的权限".getBytes());
		return response.writeWith(Flux.just(dataBuffer));
	}
}
