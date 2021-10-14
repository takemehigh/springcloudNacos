package wg.api.commons;

import wg.api.exceptions.BusinessException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiModel("请求结果")
public class ResultDTO<T> implements Serializable {
    @ApiModelProperty("是否成功")
    private Boolean success;
    @ApiModelProperty("返回代码")
    private String code;
    @ApiModelProperty("返回描述")
    private String message;
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("每页条数")
    private Integer pageSize;
    @ApiModelProperty("总页数")
    private Integer pageCount;
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("返回的实际数据")
    private T data;
    //@ApiModelProperty("字段验证错误信息")
    //private List<FieldErrorInfo> fieldErrors;
    @ApiModelProperty("元数据")
    private Map<String, Object> meta;

    public ResultDTO() {
        this.success = Boolean.TRUE;
        this.code = "OK";
        this.message = "OK";
    }

    public ResultDTO(T data) {
        this.success = Boolean.TRUE;
        this.code = "OK";
        this.message = "OK";
        this.data = data;
    }

    public ResultDTO(T data, Map<String, Object> meta) {
        this.success = Boolean.TRUE;
        this.code = "OK";
        this.message = "OK";
        this.data = data;
        this.meta = meta;
    }

    /** @deprecated */
    @Deprecated
    public static <T> ResultDTO<T> newResult() {
        return empty();
    }

    /** @deprecated */
    @Deprecated
    public static <T> ResultDTO<T> newResult(T data) {
        return ok(data);
    }

    /** @deprecated */
    @Deprecated
    public static <T> ResultDTO<T> newResult(T data, Map<String, Object> meta) {
        return ok(data, meta);
    }

    public static <T> ResultDTO<T> empty() {
        return new ResultDTO();
    }

    public static <T> ResultDTO<T> ok(T data) {
        return new ResultDTO(data);
    }

    public static <T> ResultDTO<T> ok(T data, Map<String, Object> meta) {
        return new ResultDTO(data, meta);
    }

    public static <T> ResultDTO<T> err(String code, String msg) {
        ResultDTO<T> inst = new ResultDTO();
        inst.code = code;
        inst.message = msg;
        inst.success = false;
        return inst;
    }

    public static <T> ResultDTO<T> err(String msg) {
        return err("RY00", msg);
    }

    public static <T> ResultDTO<T> err(BusinessException e) {
        ResultDTO<T> inst = new ResultDTO();
        inst.code = e.getCode();
        inst.message = e.getMessage();
        inst.success = false;
        return inst;
    }

    public static <R, D> ResultDTO<List<D>> map(Collection<R> origin, Function<R, D> mapper) {
        List<D> list = (List)origin.stream().map(mapper).collect(Collectors.toList());
        return new ResultDTO(list);
    }

    public static <R, D> ResultDTO<List<D>> map(Collection<R> origin, Function<R, D> mapper, Map<String, Object> meta) {
        List<D> list = (List)origin.stream().map(mapper).collect(Collectors.toList());
        return new ResultDTO(list, meta);
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return this.pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return this.meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public Object getMetaValue(String key) {
        return this.meta == null ? null : this.meta.get(key);
    }

    public void setMetaValue(String key, Object value) {
        if (this.meta == null) {
            this.meta = new HashMap();
        }

        this.meta.put(key, value);
    }

    /*public List<FieldErrorInfo> getFieldErrors() {
        return this.fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorInfo> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
*/
    public String toString() {
        return (new StringJoiner(", ", ResultDTO.class.getSimpleName() + "[", "]")).add("success=" + this.success).add("code='" + this.code + "'").add("message='" + this.message + "'").add("pageNum=" + this.pageNum).add("pageSize=" + this.pageSize).add("pageCount=" + this.pageCount).add("total=" + this.total).add("data=" + this.data)
                //.add("fieldErrors=" + this.fieldErrors)
                .add("meta=" + this.meta).toString();
    }
}
