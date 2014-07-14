package net.kibotu.android.deviceinfo;

import net.kibotu.android.deviceinfo.fragments.list.DeviceInfoFragment;

public enum Registry implements IGetInfoFragment {

    General(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("General", "description", "Value");

            return cachedList;
        }
    },

    Battery(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Battery", "description", "Value");

            return cachedList;
        }
    },

    Display(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Display", "description", "Value");

            return cachedList;
        }
    },

    Memory(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Memory", "description", "Value");

            return cachedList;
        }
    },

    Hardware(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Hardware", "description", "Value");

            return cachedList;
        }
    },

    Software(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Software", "description", "Value");

            return cachedList;
        }
    },

    Sensor(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Sensor", "description", "Value");

            return cachedList;
        }
    },

    Network(android.R.drawable.ic_menu_search) {

        @Override
        public DeviceInfoFragment getFragmentList() {
            if(cachedList != null) return cachedList;

            cachedList = new DeviceInfoFragment(Device.context());
            cachedList.addItem("Network", "description", "Value");

            return cachedList;
        }
    };

    public int iconR;
    protected DeviceInfoFragment cachedList;

    private Registry(final int iconR) {
        this.iconR = iconR;
    }
}

interface IGetInfoFragment {
    DeviceInfoFragment getFragmentList();
}
