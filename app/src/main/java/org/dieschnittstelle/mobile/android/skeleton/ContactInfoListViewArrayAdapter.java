package org.dieschnittstelle.mobile.android.skeleton;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.dieschnittstelle.mobile.android.skeleton.models.ContactInfo;
public class ContactInfoListViewArrayAdapter extends ArrayAdapter<ContactInfo>
{
    Context mContext;

    public ContactInfoListViewArrayAdapter(Context context)
    {
        super(context, R.layout.activity_detailview_contactinfo_view);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ContactInfo contactInfo = getItem(position);
        final View result;

        var newView = convertView == null;
        if (newView)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_detailview_contactinfo_view, parent, false);
        }

        convertView.setTag(contactInfo);

        result = convertView;

        TextView nameTV = result.findViewById(R.id.nameTV);
        Button buttonEmailContact = result.findViewById(R.id.buttonEmailContact);
        buttonEmailContact.setText(new String(Character.toChars(0x1f4e7)));
        buttonEmailContact.setVisibility(contactInfo.getEmail() == null ? View.GONE : View.VISIBLE);
        buttonEmailContact.setOnClickListener(view ->
        {
            Uri emailUri = Uri.parse("mailto:" + contactInfo.getEmail() + "?subject=Termin:" + contactInfo.getTodoItem().getName() + " am " + contactInfo.getTodoItem().getDueDateStr() + "?body=" + contactInfo.getTodoItem().getDescription());

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, emailUri);
            mContext.startActivity(emailIntent);
        });

        Button buttonSmsContact = result.findViewById(R.id.buttonSmsContact);
        buttonSmsContact.setText(new String(Character.toChars(0x1f4f1)));
        buttonSmsContact.setVisibility(contactInfo.getMobilePhone() == null ? View.GONE : View.VISIBLE);
        buttonSmsContact.setOnClickListener(view ->
        {
            Uri smsUri = Uri.parse("smsto:" + contactInfo.getMobilePhone() + "?body=Termin:" + contactInfo.getTodoItem().getName() + " am " + contactInfo.getTodoItem().getDueDateStr() + "\n" +  contactInfo.getTodoItem().getDescription());

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            mContext.startActivity(emailIntent);
        });

        Button buttonDeleteContact = result.findViewById(R.id.buttonDeleteContact);
        buttonDeleteContact.setOnClickListener(view ->
        {
            contactInfo.getParent().DeleteContact(position);
        });

        nameTV.setText(contactInfo.getName());

        return result;
    }
}
