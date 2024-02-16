# REMOTE DESKTOP APP
## Giới thiệu:
•	Ứng dụng Remote Desktop là phần mềm cho phép người dùng kết nối với máy tính ở một vị trí khác, tương tác như thể họ được kết nối cục bộ

•	Ứng dụng cũng có thể điều khiển màn hình phía server. Việc cho phép máy client sử dụng các tính năng khác nhau của phần mềm sẽ nằm trong tay máy chủ server.
## Thông tin thành viên:
• Trịnh Minh Tuấn - B20DCCN037

• Lại Ngọc Sơn - B20DCCN575

• Nguyễn Ngọc Nam - B20DCCN455
## Các công nghệ sử dụng

•	TCP Socket

•	Java Swing

•	Java AWT (Abstract Window Toolkit)

• OpenCV

## Mô tả chức năng
• Ứng dụng cho phép người dùng truy cập máy tính khác, có thể là máy tính cá nhân, máy chủ hoặc máy tính khác mà họ muốn kiểm soát từ xa

•	Chia sẻ màn hình: Màn hình của người dùng sẽ được truyền đi và hiển thị trên màn hình của người nhận. Điều này cho phép người nhận xem và điều khiển màn hình từ xa, như là việc điều khiển một máy tính từ xa.

• Điều khiển các thao tác chuột và bàn phím của máy khác

•	Chuyển tập tin: Ứng dụng cung cấp khả năng chuyển tập tin giữa người dùng gửi và người dùng nhận. Người dùng có thể chọn tập tin từ máy tính của mình và gửi nó cho người nhận thông qua ứng dụng. Người nhận sẽ nhận được tập tin và có thể lưu nó vào máy tính của mình. Chức năng này hữu ích trong việc chia sẻ và truyền tải các tệp tin quan trọng hoặc tài liệu giữa các người dùng từ xa.

• Ghi nhật kí lịch sử kết nối vào file log

• Sử dụng opencv nâng cao chất lượng hình ảnh khi chia sẻ

• Chia sẻ clipboard giữa hai máy: Cho phép việc copy paste giữa máy client với server và ngược lại

• Cho phép truỳ chọn kích thước màn hình khi chia sẻ

• Vẽ trên màn hình trong quá trình chia sẻ

•Chia sẻ drives: cho phép chia sẻ ổ đĩa (hoặc thư mục) với các máy tính khác trong cùng một mạng nội bộ. Khi chia sẻ một ổ đĩa, người dùng khác được chia sẻ sẽ truy cập và tương tác với dữ liệu có trong đó

• Cho phép một máy client có thể remote nhiều server
## Preview giao diện
• Giao diện trang chủ:

![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/ac09781b-3578-478c-9ecd-091ff75f314d)

• Giao diện trang xem lịch sử kết nối:

![lịch sử kết nối](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/2d204030-e761-4e7b-bbaf-45bfbd584b63)

• Giao diện tìm kiếm lịch sử kết nối:

![tìm kiếm lịch sử](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/fe23713f-56b7-4016-abe7-b77de30c8605)


• Giao diện chia sẻ màn hình khi máy client kết nối với máy server:

![Giao diện kết nối client](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/cd3d509d-ed5d-4973-8b20-052b2c9c1da2)

• Giao diện Menu nhận file được gửi ở máy server:

![Giao diện menu nhận file server](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/7a66718f-5143-403a-8eed-62c2034a99ab)

• Giao diện vẽ trên màn hình chia sẻ:

![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/7cf64f4c-03cf-44f7-ad60-ba6cacf8c6f5)

• Giao diện Menu tuỳ chỉnh kích thước màn hình được chia sẻ

![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/0a63f207-975d-459a-9282-2cb58d88766a)

• Gian diện màn hình khi remote nhiều máy cùng một lúc

![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/f9f27bb7-c4db-49c9-85a8-7b90a0cdb6ed)

• Giao diện chia sẻ drives thành công

![image](https://github.com/jnp2018/g3_proj-037575455/assets/94033356/6a208d64-d51b-4499-9d5a-6f2308df976a)

## Cài đặt môi trường
• Java Development Kit (JDK): JDK 8 trở lên

• Cài đặt thư viện OpenCV trước khi chạy
## Triển khai
• Chạy file remotedesktop.exe để chạy chương trình

• Sử dụng thư viện launch4j để tích hợp vào quy trình xây dựng Maven, cho tạo tệp thực thi .exe khi bạn xây dựng dự án.
