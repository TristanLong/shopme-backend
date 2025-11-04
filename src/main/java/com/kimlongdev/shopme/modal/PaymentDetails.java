package com.kimlongdev.shopme.modal;

import com.kimlongdev.shopme.domain.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_details")
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // --- 1. THÔNG TIN NỘI BỘ (Chung cho mọi cổng) ---

    /**
     * ID thanh toán duy nhất do hệ thống của BẠN tạo ra.
     * Đây là mã "paymentId" gốc của bạn.
     */
    private String paymentId;

    /**
     * Trạng thái thanh toán trong hệ thống của BẠN.
     * Ví dụ: PENDING, SUCCESS, FAILED.
     * Đây là trường "status" gốc của bạn.
     */
    private PaymentStatus status;

    /**
     * Số tiền của giao dịch.
     */
    private long amount;

    /**
     * Đơn vị tiền tệ (ví dụ: "USD", "VND", "INR").
     */
    private String currency;

    // --- 2. THÔNG TIN TỪ CỔNG THANH TOÁN (Linh hoạt) ---

    /**
     * Tên của cổng thanh toán đã xử lý giao dịch.
     * Ví dụ: "STRIPE", "RAZORPAY", "PAYPAL".
     */
    private String gatewayProvider;

    /**
     * ID giao dịch chính mà cổng thanh toán trả về.
     * Đây có thể là Payment Intent ID (pi_...) của Stripe.
     * Hoặc là Payment ID (pay_...) của Razorpay.
     */
    private String gatewayTransactionId;

    /**
     * Trạng thái gốc mà cổng thanh toán trả về.
     * Ví dụ: "succeeded" (Stripe), "paid" (Razorpay).
     * Bạn sẽ dựa vào trạng thái này để cập nhật trường "status" nội bộ ở trên.
     */
    private String gatewayStatus;

    /**
     * (Tùy chọn) Dùng để lưu các thông tin phụ khác.
     * Ví dụ: lưu "razorpayPaymentLinkId" hoặc "stripeSessionId".
     * Nếu dùng CSDL, trường này có thể là kiểu JSON/JSONB.
     */
    @ElementCollection
    @CollectionTable(name = "payment_additional_data",
            joinColumns = @JoinColumn(name = "payment_id")) // Khóa ngoại trỏ về bảng payment_details
    @MapKeyColumn(name = "data_key") // Tên cột cho Key của Map
    @Column(name = "data_value") // Tên cột cho Value của Map
    private Map<String, String> additionalData;
}
