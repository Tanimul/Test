package bd.com.media365.ratehammer_sdk.ui.fragments.loan.new_loan

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import bd.com.media365.ratehammer_sdk.R
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import timber.log.Timber
import kotlin.math.cos
import kotlin.math.sin


class RadarView(private val context: Context, attrs: AttributeSet?) : View(context, attrs) {

    //<editor-fold desc="Properties Init">
    private val outerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_F8FAFC)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val circlePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.color_E2E8F0)
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    private val activeTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_0F172A)
        textSize = 16f
        textAlign = Paint.Align.CENTER
    }

    private val inactiveTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.color_94A3B8)
        textSize = 16f
        textAlign = Paint.Align.CENTER
    }

    // Define circle properties
    var totalCircle: Int = 1
    var outerCircleRadius: Float = 320f
    var gapBetweenCircles: Float = 32f
    var centerImage: Drawable? = null

    var centerImageSize: Float = 0f

    var radarValue: ArrayList<RadarModel> = arrayListOf() // List of value
    //</editor-fold>

    //<editor-fold desc="Drawing Canvas">
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f

        canvas.save()

        // Draw the cross within the bounds of the outer circle
        drawCross(canvas, centerX, centerY, outerCircleRadius)

        // Draw the outer circle
        canvas.drawCircle(centerX, centerY, outerCircleRadius, outerCirclePaint)

        //Draw all circle
        drawCircle(canvas, centerX, centerY)

        //Draw all point
        drawPoint(canvas, centerX, centerY)

        //Draw image
        drawImage(canvas, centerX, centerY)

        // Restore the canvas transformation
        canvas.restore()
    }

    //<editor-fold desc="Draw Cross">
    private fun drawCross(canvas: Canvas, centerX: Float, centerY: Float, radius: Float) {
        val crossSize = radius * 0.7f // Adjust the size of the cross

        val crossPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.color_F1F5F9)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        // Draw first diagonal line of the cross
        canvas.drawLine(
            centerX - crossSize,
            centerY - crossSize,
            centerX + crossSize,
            centerY + crossSize,
            crossPaint
        )

        // Draw second diagonal line of the cross
        canvas.drawLine(
            centerX + crossSize,
            centerY - crossSize,
            centerX - crossSize,
            centerY + crossSize,
            crossPaint
        )

    }
    //</editor-fold>

    //<editor-fold desc="Draw Circle">
    private fun drawCircle(canvas: Canvas, centerX: Float, centerY: Float) {

        val density = context.resources.displayMetrics.density

        for (i in 1..totalCircle) {
            val radius = outerCircleRadius - i * (gapBetweenCircles * density)

            // Calculate the alpha value for the current circle
            val alpha = (255 * (i.toDouble() / totalCircle.toDouble())).toInt()

            // Set the alpha value for the circlePaint
            circlePaint.alpha = alpha

            canvas.drawCircle(centerX, centerY, radius, circlePaint)

            centerImageSize = if (i == totalCircle) radius else centerImageSize
        }
    }
    //</editor-fold>

    //<editor-fold desc="Draw Image">
    private fun drawImage(canvas: Canvas, centerX: Float, centerY: Float) {
        // Draw the center image inside the third inner circle

        // Set up a paint for the shadow
        val shadowPaint = Paint()
        shadowPaint.color = 0xFF1D25 // Set the shadow color (semi-transparent black)
        shadowPaint.alpha = 128 // Set the alpha value (50% opacity)
        shadowPaint.isAntiAlias = true
        shadowPaint.setShadowLayer(16f, 0f, 0f, shadowPaint.color)

        // Calculate the position and size of the shadow circle
        val shadowRadius = centerImageSize / 2 + 0f // Adjust the shadow offset as needed

        // Draw the circular shadow
        canvas.drawCircle(centerX, centerY, shadowRadius, shadowPaint)

        centerImage?.let {
            val drawableWidth = centerImageSize
            val drawableHeight = centerImageSize

            val left = centerX - drawableWidth / 2
            val top = centerY - drawableHeight / 2

            it.setBounds(
                left.toInt(),
                top.toInt(),
                (left + drawableWidth).toInt(),
                (top + drawableHeight).toInt()
            )
            it.draw(canvas)
        }
    }

    //</editor-fold>

    //<editor-fold desc="Draw Point">
    private fun drawPoint(canvas: Canvas, centerX: Float, centerY: Float) {
        var remainingTotalPoint = radarValue.size
        val density = context.resources.displayMetrics.density
        var initPointCircle = if (remainingTotalPoint > 2) 2 else remainingTotalPoint
        var startPoint = 0

        for (i in totalCircle downTo 1) {

            val radius = outerCircleRadius - i * (gapBetweenCircles * density)

            if (remainingTotalPoint > 0) {
                val numPoints = initPointCircle
                remainingTotalPoint -= numPoints
                initPointCircle += 2

                if (remainingTotalPoint < initPointCircle) {
                    initPointCircle = remainingTotalPoint
                }
                drawPointsWithText(
                    canvas,
                    centerX + 1,
                    centerY + 1,
                    radius,
                    numPoints,
                    startPoint
                )
                startPoint += numPoints
                Timber.d(
                    "drawCircle: $numPoints - $remainingTotalPoint -$initPointCircle -$startPoint"
                )
            }

        }
    }

    private fun drawPointsWithText(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        radius: Float,
        numPoints: Int,
        startPoint: Int
    ) {
        val angleStep = (2 * Math.PI / numPoints).toFloat()

        for (i in startPoint until numPoints + startPoint) {
            val angle = i * angleStep

            // Rotate the points of the third circle by 45 degrees
            val rotationAngle = if (i in 2..5) Math.PI / 4 else 0.0
            val rotatedAngle = angle + rotationAngle

            val x = centerX + radius * cos(rotatedAngle).toFloat()
            val y = centerY + radius * sin(rotatedAngle).toFloat()

            val text = radarValue[i].name
            val isPointActive = radarValue[i].isActive

            // Draw text using the appropriate Paint object
            val textPaint = if (isPointActive) activeTextPaint else inactiveTextPaint

            if (isPointActive) {
                // Create a LottieAnimationView for active points
                val lottieView = LottieAnimationView(context)
                lottieView.setAnimation("ripple_dot_green.json") // Replace with your Lottie animation resource
                lottieView.repeatCount = LottieDrawable.INFINITE

                // Calculate position and size for the LottieAnimationView
                val lottieWidth = 30 // Set your desired width
                val lottieHeight = 30 // Set your desired height

                // Calculate the position based on (x, y) as the center point
                val lottieLeft = (x - lottieWidth / 2).toInt()
                val lottieTop = (y - lottieHeight / 2).toInt()


                // Set layout parameters for the LottieAnimationView
                val layoutParams = ViewGroup.LayoutParams(lottieWidth, lottieHeight)
                lottieView.layoutParams = layoutParams

                // Add the LottieAnimationView to the parent view or layout
                // For example, if your RadarView is inside a ViewGroup, add it like this:
                (parent as? ViewGroup)?.addView(lottieView)

                // Set the position of the LottieAnimationView
                lottieView.x = lottieLeft.toFloat()
                lottieView.y = lottieTop.toFloat()

                // Start the Lottie animation
                lottieView.playAnimation()
                lottieView.draw(canvas)

                // Draw other text or elements as needed
                val lines = text.split(" ").chunked(2) // Split text into groups of 2 words
                var currentY = y + lottieHeight + 6
                for (text in lines) {
                    val lineText = text.joinToString(" ")

                    canvas.drawText(
                        lineText,
                        x,
                        lottieView.height + currentY,
                        textPaint
                    )


                    currentY += 24 // Move to the next line
                }
            } else {
                // Draw inactive points using your custom point drawable
                val pointDrawable = ContextCompat.getDrawable(context, R.drawable.custom_point_2)

                pointDrawable?.let {

                    it.setBounds(
                        (x - it.intrinsicWidth / 2).toInt(),
                        (y - it.intrinsicHeight / 2).toInt(),
                        (x + it.intrinsicWidth / 2).toInt(),
                        (y + it.intrinsicHeight / 2).toInt()
                    )

                    it.draw(canvas)
                }

                // Draw text for inactive points
                val lines = text.split(" ").chunked(2) // Split text into groups of 2 words
                var currentY = y + 6
                for (text in lines) {
                    val lineText = text.joinToString(" ")
                    if (pointDrawable != null) {
                        canvas.drawText(
                            lineText,
                            x,
                            pointDrawable.intrinsicHeight + currentY,
                            textPaint
                        )
                    }

                    currentY += 24 // Move to the next line
                }
            }


        }
    }
    //</editor-fold>
    //</editor-fold>

}