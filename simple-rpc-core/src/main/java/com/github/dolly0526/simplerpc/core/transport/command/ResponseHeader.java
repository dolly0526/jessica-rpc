package com.github.dolly0526.simplerpc.core.transport.command;

import com.github.dolly0526.simplerpc.common.constant.SimpleRpcConst;
import lombok.*;

/**
 * 返回的响应Header
 *
 * @author yusenyang
 * @create 2021/3/8 18:41
 */
@Getter
@Setter
@NoArgsConstructor
public class ResponseHeader extends Header {

    // 响应状态
    private int code;

    // 各种错误
    private String error;


    @Override
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                SimpleRpcConst.DEFAULT_LENGTH_FIELD + /** 除了上述4个属性，还需要传输错误信息的长度 **/
                (error == null ? 0 : error.getBytes(SimpleRpcConst.DEFAULT_CHARSET).length);
    }


    public ResponseHeader(int type, int version, int requestId, Throwable throwable) {
        this(type, version, requestId, Code.UNKNOWN_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(int type, int version, int requestId) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader(int type, int version, int requestId, int code, String error) {
        super(type, version, requestId);
        this.code = code;
        this.error = error;
    }
}
