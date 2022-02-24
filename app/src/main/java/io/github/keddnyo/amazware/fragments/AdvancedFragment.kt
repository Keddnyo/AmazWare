package io.github.keddnyo.amazware.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import io.github.keddnyo.amazware.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception


class AdvancedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_advanced, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.advanced) // New Title

        // Variables
        val deviceSource =  requireActivity().findViewById<EditText>(R.id.deviceSource)
        val productionSource =  requireActivity().findViewById<EditText>(R.id.productionSource)
        val appName =  requireActivity().findViewById<EditText>(R.id.appName)
        val appVersion =  requireActivity().findViewById<EditText>(R.id.appVersion)
        val appVersionBuild =  requireActivity().findViewById<EditText>(R.id.appVersionBuild)

        val appRadioGroup =  requireActivity().findViewById<RadioGroup>(R.id.appRadioGroup)

        val channelPlay =  requireActivity().findViewById<CheckBox>(R.id.channelPlay)

        val buttonReset =  requireActivity().findViewById<Button>(R.id.buttonReset)
        val buttonSubmit =  requireActivity().findViewById<Button>(R.id.buttonSubmit)

        val responseField = requireActivity().findViewById<TextView>(R.id.responseField)

        val radioChecked = appRadioGroup.checkedRadioButtonId

        appRadioGroup.setOnCheckedChangeListener { _, checkedId -> // find which radio button is selected
            if (checkedId == R.id.radioZepp) {
                appName.setText("com.huami.midong")
                channelPlay.isEnabled = true
            } else if (checkedId == R.id.radioMiFit) {
                channelPlay.isEnabled = false
                appName.setText("com.xiaomi.hm.health")
            }
        }

        val playPostfix = when (channelPlay.isChecked) {
            true -> {
                "-play"
            }
            false -> {
                ""
            }
        }

        buttonReset.setOnClickListener {
            responseField.visibility = View.GONE
        }

        buttonSubmit.setOnClickListener {

            val okHttpClient = OkHttpClient()
            val requestHost = "api-mifit-ru.huami.com"

            val uriBuilder: Uri.Builder = Uri.Builder()
            uriBuilder.scheme("https")
                .authority(requestHost)
                .appendPath("devices")
                .appendPath("ALL")
                .appendPath("hasNewVersion")

                /**.appendQueryParameter("productId", "12")
                .appendQueryParameter("vendorSource", "1")
                .appendQueryParameter("resourceVersion", "0")
                .appendQueryParameter("firmwareFlag", "0")
                .appendQueryParameter("vendorId", "0")
                .appendQueryParameter("resourceFlag", "0")
                .appendQueryParameter("productionSource", "256")
                .appendQueryParameter("userid", "0")
                .appendQueryParameter("userId", "0")
                .appendQueryParameter("deviceSource", "")
                .appendQueryParameter("fontVersion", "0")
                .appendQueryParameter("fontFlag", "0")
                .appendQueryParameter("appVersion", "6.1.4-play_100440")
                .appendQueryParameter("appid", "0")
                .appendQueryParameter("callid", "0")
                .appendQueryParameter("channel", "0")
                .appendQueryParameter("country", "0")
                .appendQueryParameter("cv", "0")
                .appendQueryParameter("device", "")
                .appendQueryParameter("deviceType", "ALL")
                .appendQueryParameter("device_type", "0")
                .appendQueryParameter("firmwareVersion", "0")
                .appendQueryParameter("hardwareVersion", "0")
                .appendQueryParameter("lang", "0")
                .appendQueryParameter("support8Bytes", "true")
                .appendQueryParameter("timezone", "0")
                .appendQueryParameter("v", "0")
                .appendQueryParameter("gpsVersion", "0")
                .appendQueryParameter("baseResourceVersion", "0")**/

                .appendQueryParameter("productId", "0")
                .appendQueryParameter("vendorSource", "1")
                .appendQueryParameter("resourceVersion", "0")
                .appendQueryParameter("firmwareFlag", "1")
                .appendQueryParameter("vendorId", "343")
                .appendQueryParameter("resourceFlag", "7")
                .appendQueryParameter("productionSource", productionSource.text.toString())
                .appendQueryParameter("userid", "8719393185")
                .appendQueryParameter("userId", "8719393185")
                .appendQueryParameter("deviceSource", deviceSource.text.toString())
                .appendQueryParameter("fontVersion", "0")
                .appendQueryParameter("fontFlag", "3")
                .appendQueryParameter("appVersion", appVersion.text.toString() + playPostfix + appVersionBuild.text.toString())
                .appendQueryParameter("appid", "2882303761517383915")
                .appendQueryParameter("callid", "1614431188364")
                .appendQueryParameter("channel", "play")
                .appendQueryParameter("country", "CH")
                .appendQueryParameter("cv", "100395_5.12.2-play")
                .appendQueryParameter("device", "android_29")
                .appendQueryParameter("deviceType", "ALL")
                .appendQueryParameter("device_type", "android_phone")
                .appendQueryParameter("firmwareVersion", "0")
                .appendQueryParameter("hardwareVersion", "0.62.130.16")
                .appendQueryParameter("lang", "zh_CH")
                .appendQueryParameter("support8Bytes", "true")
                .appendQueryParameter("timezone", "Europe/Moscow")
                .appendQueryParameter("v", "2.0")
                .appendQueryParameter("gpsVersion", "0")
                .appendQueryParameter("baseResourceVersion", "0")

            val request = Request.Builder()
                .url(uriBuilder.toString())

                /**.addHeader("hm-privacy-diagnostics", "false")
                .addHeader("country", "CH")
                .addHeader("appplatform", "android_phone")
                .addHeader("hm-privacy-ceip", "0")
                .addHeader("X-Request-Id", "0")
                .addHeader("timezone", "0")
                .addHeader("channel", "play")
                .addHeader("User-Agent", "0")
                .addHeader("cv", "0")
                .addHeader("appname", "com.huami.midong")
                .addHeader("v", "0")
                .addHeader("apptoken", "0")
                .addHeader("lang", "zh_CH")
                .addHeader("Host", requestHost)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Accept-Encoding", "0")**/

                .addHeader("hm-privacy-diagnostics", "false")
                .addHeader("country", "AR")
                .addHeader("appplatform", "android_phone")
                .addHeader("hm-privacy-ceip", "true")
                .addHeader("x-request-id", "679b1bad-1537-4ebf-bc94-d61424855df2")
                .addHeader("timezone", "Europe/Moscow")
                .addHeader("channel", "play")
                .addHeader("user-agent", "Zepp/5.12.2-play \\(Sharp Aquos S2 4/64; Android 10; Density/2.1000001\\)")
                .addHeader("cv", "100395_5.12.2-play")
                .addHeader("appname", appName.text.toString())
                .addHeader("v", "2.0")
                .addHeader("apptoken", "0")
                .addHeader("lang", "ar_AR")
                .addHeader("Host", "api-mifit-ru.huami.com")
                .addHeader("accept-encoding", "gzip")
                .addHeader("accept", "*/*")

                .build()



            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call, response: Response) {
                    val json = JSONObject(response.body()!!.string())

                    responseField.post {
                        responseField.visibility = View.VISIBLE
                        responseField.text = json.toString()
                    }
                }
            })







            /**HttpClient client = new HttpClient();
            HttpRequestMessage request = new HttpRequestMessage();

            string request_host = "api-mifit-ru.huami.com";

            UriBuilder uriBuilder = new UriBuilder
            {
            Scheme = "https",
            Host = request_host,
            Path = "devices/ALL/hasNewVersion",
            Query = "productId=71&vendorSource=1&resourceVersion=0&firmwareFlag=0&vendorId=0&resourceFlag=0&productionSource=" + production_text.Text + "&userid=0&userId=0&deviceSource=" + model_text.Text + "&fontVersion=0&fontFlag=0&appVersion=" + app_version_number_text.Text + play_postfix + "_" + app_version_build_text.Text + "&appid=0&callid=0&channel=0&country=0&cv=0&device=0&deviceType=ALL&device_type=0&firmwareVersion=0&hardwareVersion=0&lang=0&support8Bytes=true&timezone=0&v=0",
            };
            Uri URL = uriBuilder.Uri;
            String stringUri;
            stringUri = URL.ToString();

            request.RequestUri = new Uri(stringUri);
            request.Method = HttpMethod.Get;

            request.Headers.Add("hm-privacy-diagnostics", "false");
            request.Headers.Add("country", "CH");
            request.Headers.Add("appplatform", "android_phone");
            request.Headers.Add("hm-privacy-ceip", "0");
            request.Headers.Add("X-Request-Id", "0");
            request.Headers.Add("timezone", "0");
            request.Headers.Add("channel", "play");
            request.Headers.Add("User-Agent", "0");
            request.Headers.Add("cv", "0");
            request.Headers.Add("appname", app_name_text.Text);
            request.Headers.Add("v", "0");
            request.Headers.Add("apptoken", "0");
            request.Headers.Add("lang", "zh_CH");
            request.Headers.Add("Host", request_host);
            request.Headers.Add("Connection", "Keep-Alive");
            request.Headers.Add("Accept-Encoding", "0");

            HttpResponseMessage response = await client.SendAsync(request);
            if (response.StatusCode == HttpStatusCode.OK)
            {
            HttpContent responseContent = response.Content;

            var server_response = await responseContent.ReadAsStringAsync();
            //response_text.Text = server_response;

            Response content = JsonConvert.DeserializeObject<Response>(server_response);
            string log = "";
            if (content.changeLog != null)
            {
            log = "Click to show log";
            }
            else
            {
            log = "Log not founded";
            }

            ObservableCollection<string> data = new ObservableCollection<string>
            {
            "Firmware version: " + content.firmwareVersion + "\n" + "MD5: " + content.firmwareMd5,
            "Resource version: " + content.resourceVersion.ToString() + "\n" + "MD5: " + content.resourceMd5,
            "Font version: " + content.fontVersion.ToString() + "\n" + "MD5: " + content.fontMd5,
            "Languages: " + content.lang,
            log
            //content.deviceType,
            //content.deviceSource,
            //content.firmwareLength,
            //content.firmwareFlag,
            //content.fontLength,
            //content.fontFlag,
            //content.resourceFlag,
            //content.resourceLength,
            //content.productionSource,
            //content.changeLog,
            //content.upgradeType,
            //content.buildTime,
            //content.ignore,
            //content.support8Bytes,
            //content.downloadBackupPaths
            };

            adapter = new ArrayAdapter<string>(this, Android.Resource.Layout.SimpleListItem1, data);
            response_listview.TextFilterEnabled = false;

            if (content.buildTime != 0)
            {
            response_listview.Visibility = Android.Views.ViewStates.Visible;
            response_listview.Adapter = adapter;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            }
            else
            {
            response_listview.Visibility = Android.Views.ViewStates.Gone;
            clear_response_button.Visibility = Android.Views.ViewStates.Gone;
            Toast.MakeText(Application, "Firmware not found", ToastLength.Short).Show();
            }


            response_listview.ItemClick += delegate (object sender, AdapterView.ItemClickEventArgs args)
            {
            //Toast.MakeText(Application, ((TextView)args.View).Text, ToastLength.Short).Show();


            if (args.Position.ToString() == "0")
            {
            response_text.Text = content.firmwareVersion + " : " + content.firmwareMd5;
            editor.PutString("content_MD5", content.firmwareMd5);
            editor.PutString("content_URL", content.firmwareUrl);
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Visible;
            download_button.Visibility = Android.Views.ViewStates.Visible;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            else if (args.Position.ToString() == "1")
            {
            if (content.resourceVersion != 0)
            {
            response_text.Text = content.resourceVersion + " : " + content.resourceMd5;
            editor.PutString("content_MD5", content.resourceMd5);
            editor.PutString("content_URL", content.resourceUrl);
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Visible;
            download_button.Visibility = Android.Views.ViewStates.Visible;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            else
            {
            response_text.Text = GetString(Resource.String.not_available);
            editor.PutString("content_MD5", "");
            editor.PutString("content_URL", "");
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Gone;
            download_button.Visibility = Android.Views.ViewStates.Gone;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            }
            else if (args.Position.ToString() == "2")
            {
            response_text.Text = content.fontVersion + " : " + content.fontMd5;
            editor.PutString("content_MD5", content.fontMd5);
            editor.PutString("content_URL", content.fontUrl);
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Visible;
            download_button.Visibility = Android.Views.ViewStates.Visible;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            else if (args.Position.ToString() == "3")
            {
            response_text.Text = content.lang;
            editor.PutString("content_MD5", "");
            editor.PutString("content_URL", "");
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Gone;
            download_button.Visibility = Android.Views.ViewStates.Gone;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            else if (args.Position.ToString() == "4")
            {
            response_text.Text = content.changeLog;
            editor.PutString("content_MD5", "");
            editor.PutString("content_URL", "");
            editor.Apply();

            copy_MD5_button.Visibility = Android.Views.ViewStates.Gone;
            download_button.Visibility = Android.Views.ViewStates.Gone;
            clear_response_button.Visibility = Android.Views.ViewStates.Visible;
            response_text_layout.Visibility = Android.Views.ViewStates.Visible;
            response_text.Visibility = Android.Views.ViewStates.Visible;
            }
            //response_text.Text = ((TextView)args.View).Text;
            };
            }**/

        }
    }
}