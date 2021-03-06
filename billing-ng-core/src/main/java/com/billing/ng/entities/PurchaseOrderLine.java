/*
 BillingNG, a next-generation billing solution
 Copyright (C) 2011 Brian Cowdery

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation, either version 3 of the
 License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see http://www.gnu.org/licenses/agpl-3.0.html
 */

package com.billing.ng.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * Purchase order line representing the addition of a product, service, or
 * aggregation of charges into an order.
 *
 * @author Brian Cowdery
 * @since 2-Dec-2010
 */
@Entity
@XmlRootElement
public class PurchaseOrderLine extends BaseEntity implements Totaled {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @OneToMany(mappedBy = "line")
    private List<Charge> charges = new ArrayList<Charge>();

    @Embedded
    private Money total;

    public PurchaseOrderLine() {
    }

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    @XmlElement
    @XmlElementWrapper(name = "charges")
    public List<Charge> getCharges() {
        return charges;
    }

    public void setCharges(List<Charge> charges) {
        this.charges = charges;
    }

    @XmlElement
    public Money getTotal() {
        if (total == null)
            calculateTotal();

        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    public void calculateTotal() {
        for (Charge charge : getCharges())
            total = total.add(charge.getAmount());
    }
}
