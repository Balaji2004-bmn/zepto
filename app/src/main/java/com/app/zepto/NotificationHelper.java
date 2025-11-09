package com.app.zepto;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "zepto_channel";
    private static final String CHANNEL_NAME = "Zepto Notifications";
    private static final String CHANNEL_DESCRIPTION = "Notifications for order updates and promotions";

    private Context context;
    private NotificationManager notificationManager;

    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(android.graphics.Color.GREEN);
            channel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showOrderNotification(String title, String message, String orderId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_zepto_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentIntent(createOrderIntent(orderId));

        notificationManager.notify(orderId.hashCode(), builder.build());
    }

    public void showPromotionalNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_zepto_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.colorPrimary));

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    public void showOrderStatusNotification(String orderId, String status) {
        String title = "Order Update";
        String message = "Your order #" + orderId + " is now " + status;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_zepto_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(getStatusColor(status))
                .setContentIntent(createOrderIntent(orderId));

        notificationManager.notify(orderId.hashCode(), builder.build());
    }

    private android.app.PendingIntent createOrderIntent(String orderId) {
        Intent intent = new Intent(context, OrdersActivity.class);
        intent.putExtra("order_id", orderId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return android.app.PendingIntent.getActivity(
                context,
                orderId.hashCode(),
                intent,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE
        );
    }

    private int getStatusColor(String status) {
        switch (status.toLowerCase()) {
            case "delivered":
                return context.getResources().getColor(R.color.green);
            case "shipped":
                return context.getResources().getColor(R.color.blue);
            case "processing":
                return context.getResources().getColor(R.color.orange);
            case "cancelled":
                return context.getResources().getColor(R.color.red);
            default:
                return context.getResources().getColor(R.color.colorPrimary);
        }
    }

    // Static helper methods for quick notifications
    public static void notifyOrderPlaced(Context context, String orderId, double amount) {
        NotificationHelper helper = new NotificationHelper(context);
        String title = "Order Placed Successfully!";
        String message = "Your order #" + orderId + " for ₹" + amount + " has been confirmed.";
        helper.showOrderNotification(title, message, orderId);
    }

    public static void notifyOrderStatusUpdate(Context context, String orderId, String status) {
        NotificationHelper helper = new NotificationHelper(context);
        helper.showOrderStatusNotification(orderId, status);
    }

    public static void notifyPromotion(Context context, String title, String message) {
        NotificationHelper helper = new NotificationHelper(context);
        helper.showPromotionalNotification(title, message);
    }

    public static void notifyDelivery(Context context, String orderId, String deliveryTime) {
        NotificationHelper helper = new NotificationHelper(context);
        String title = "Delivery Update";
        String message = "Your order #" + orderId + " will be delivered by " + deliveryTime;
        helper.showOrderNotification(title, message, orderId);
    }
}