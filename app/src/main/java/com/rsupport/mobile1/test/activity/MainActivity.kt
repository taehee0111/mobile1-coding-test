package com.rsupport.mobile1.test.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.rsupport.mobile1.test.gettyimage.adapter.GetGettyAdapter
import android.os.Bundle
import com.rsupport.mobile1.test.R
import com.google.android.gms.security.ProviderInstaller
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import android.widget.ProgressBar
import com.rsupport.mobile1.test.utils.manager.NetworkManager
import android.os.Parcelable
import android.util.Log
import android.view.View
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult
import com.rsupport.mobile1.test.db.RoomDatabase
import java.util.*

//테스트:
// 네트워크 연결 상태
// 페이지 로딩 실패 유무
// 화면 회전
class MainActivity : AppCompatActivity() {
    @JvmField
    var rv: RecyclerView? = null
    private var mActivity: MainActivity? = null
    private var adapter: GetGettyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mActivity = this
        //보안패치 (API 19 통신 보안 업데이트 => ssl을 통한 보안 )
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            checkGooglePlayServices()
            Log.e(TAG, e.toString())
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e(TAG, "Google Play Services not available.")
        }
        rv = findViewById(R.id.rv)
        val pb = findViewById<ProgressBar>(R.id.pb)


        RoomDatabase room
        //서비스 실행전 네트워크 상태설정
        NetworkManager.getNetworkStatus(mActivity)

        //리스트 뷰 설정
        adapter = GetGettyAdapter(mActivity)
        pb.visibility = View.VISIBLE
        //parcelable 예외처리
        if (savedInstanceState == null) {
            //초기 페이지 로딩 호출
            adapter!!.loadPage(null) { pb.visibility = View.GONE }
        } else {
            pb.visibility = View.GONE
        }
    } //end onCreate

    //화면 회전시 데이터 유지
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (adapter != null && adapter!!.gridLayoutManager != null) {
            val rvParcel = adapter!!.gridLayoutManager.onSaveInstanceState()
            outState.putParcelable(RECYCLER_VIEW_POSITION, rvParcel)
            outState.putParcelableArrayList(RECYCLER_VIEW_LIST, adapter!!.resultLists)
        }
    }

    //화면 회전시 데이터 유지
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (adapter != null && adapter!!.gridLayoutManager != null) {
            val rvParcel = savedInstanceState.getParcelable<Parcelable>(RECYCLER_VIEW_POSITION)
            adapter!!.resultLists = savedInstanceState.getParcelableArrayList(RECYCLER_VIEW_LIST)
            adapter!!.gridLayoutManager.onRestoreInstanceState(rvParcel)
        }
    }

    //jellyBean 구글 플레이에서 지원 중단(API 16)
    private fun checkGooglePlayServices() {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
//            ToastUtils.showToast(mActivity,getResources().getString(R.string.jellybean_not_able));
//        }
        when (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)) {
            ConnectionResult.SERVICE_MISSING -> Objects.requireNonNull(
                GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, ConnectionResult.SERVICE_MISSING, 0)
            )?.show()
            ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> Objects.requireNonNull(
                GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, 0)
            )?.show()
            ConnectionResult.SERVICE_DISABLED -> Objects.requireNonNull(
                GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, ConnectionResult.SERVICE_DISABLED, 0)
            )?.show()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val RECYCLER_VIEW_POSITION = "RECYCLER_VIEW_POSITION"
        private const val RECYCLER_VIEW_LIST = "RECYCLER_VIEW_POSITION"
    }
}