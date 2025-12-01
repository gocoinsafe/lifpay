package org.hcm.lifpay.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPage<T> implements Serializable {
    private long count;
    private List<T> list;
    private long pageNumber;
    private long pageSize;
}
