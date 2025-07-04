package com.field2fork.dtos;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.field2fork.pojos.OrderStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderResponseDTO {
	private Long id;
	private Timestamp order_date;
	private BigDecimal totalAmount;
	private OrderStatus orderStatus;
	private Timestamp deliveryDate;
	public OrderResponseDTO(Long order_id, Timestamp order_date, BigDecimal totalAmount, OrderStatus orderStatus,
			Timestamp deliveryDate) {
		this.id =order_id;
		this.order_date = order_date;
		this.totalAmount = totalAmount;
		this.orderStatus = orderStatus;
		this.deliveryDate = deliveryDate;
	}
}
