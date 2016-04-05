package de.tomade.saufomat2.activity.miniGames;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import de.tomade.saufomat2.R;

//TODO
public class KistenStapelnActivity extends Activity implements View.OnTouchListener {

    private int screenWidth;
    private int screenHeight;

    private List<ImageView> towerImageList = new ArrayList<>();
    private ImageView currentCrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisten_stapeln);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        this.currentCrate = (ImageView) this.findViewById(R.id.beerCrate0);
        this.currentCrate.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                int xCoord = (int) event.getRawX();
                int yCoord = (int) event.getRawY();

                if (xCoord > screenWidth) {

                    xCoord = screenWidth;
                }

                if (yCoord > screenHeight) {

                    yCoord = screenHeight;
                }

                v.setLayoutParams(layoutParams);

                break;

            default:

                break;
        }
        return true;
    }

    private final class MyClickListener implements View.OnLongClickListener {

        // called when the item is long-clicked
        @Override
        public boolean onLongClick(View view) {
            // TODO Auto-generated method stub

            // create it from the object's tag
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, //data to be dragged
                    shadowBuilder, //drag shadow
                    view, //local data about the drag and drop operation
                    0   //no needed flags
            );


            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    /*class MyDragListener implements View.OnDragListener {
        Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);
        Drawable targetShape = getResources().getDrawable(R.drawable.target_shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {

            // Handles each of the expected events
            switch (event.getAction()) {


                //signal for the start of a drag and drop operatin.
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;

                //the drag point has entered the bounding box of the View
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(targetShape);   //change the shape of the view
                    break;

                //the user has moved the drag shadow outside the bounding box of the View
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(normalShape);   //change the shape of the view back to normal
                    break;

                //drag shadow has been released,the drag point is within the bounding box of the View
                case DragEvent.ACTION_DROP:
                    // if the view is the bottomlinear, we accept the drag item
                    if(v == findViewById(R.id.bottomlinear)) {
                        View view = (View) event.getLocalState();
                        ViewGroup viewgroup = (ViewGroup) view.getParent();
                        viewgroup.removeView(view);

                        //change the text
                        TextView text = (TextView) v.findViewById(R.id.text);
                        text.setText("The item is dropped");

                        LinearLayout containView = (LinearLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);
                    } else {
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context, "You can't drop the image here",
                                Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;

                //the drag and drop operation has concluded.
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(normalShape);   //go back to normal shape

                default:
                    break;
            }
            return true;
        }*/

}
