package com.codesndata.aickongowea.covid_19;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codesndata.aickongowea.R;

import java.util.List;

/**
 * https://www.codesndata.com
 */
public class ViewAdapter extends BaseAdapter {
  Context context;
  List<Attendee> valueList;

  public ViewAdapter(List<Attendee> listValue, Context context)
  {
    this.context = context;
    this.valueList = listValue;
  }

  @Override
  public int getCount()
  {
    return this.valueList.size();
  }

  @Override
  public Object getItem(int position)
  {
    return this.valueList.get(position);
  }

  @Override
  public long getItemId(int position)
  {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    ViewData viewItem;

    if(convertView == null)
    {
      viewItem = new ViewData();

      LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

      convertView = layoutInfiater.inflate(R.layout.data_item, null);

      viewItem.TextViewPos = convertView.findViewById(R.id.pos_tv);
      viewItem.TextViewFName = convertView.findViewById(R.id.fname_tv);
      viewItem.TextViewLName = convertView.findViewById(R.id.lname_tv);
      viewItem.TextViewService = convertView.findViewById(R.id.service_tv);
      viewItem.TextViewTBooked = convertView.findViewById(R.id.tbooked_tv);

      convertView.setTag(viewItem);
    }
    else
    {
      viewItem = (ViewData) convertView.getTag();
    }

    viewItem.TextViewPos.setText(valueList.get(position).pos);
    viewItem.TextViewFName.setText(valueList.get(position).FName);
    viewItem.TextViewLName.setText(valueList.get(position).LName);
    viewItem.TextViewService.setText(valueList.get(position).Service);
    viewItem.TextViewTBooked.setText(valueList.get(position).TBooked);
    return convertView;
  }
}

class ViewData
{
  TextView TextViewPos;
  TextView TextViewFName;
  TextView TextViewLName;
  TextView TextViewService;
  TextView TextViewTBooked;
}