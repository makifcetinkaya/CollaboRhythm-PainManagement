package collaboRhythm.shared.model.healthRecord.document
{

	import collaboRhythm.shared.model.*;


    [Bindable]
    public class EquipmentScheduleItem extends ScheduleItemBase
	{
		public static const DOCUMENT_TYPE:String = "http://indivo.org/vocab/xml/documents#EquipmentScheduleItem";
        public static const EQUIPMENT:String = "Equipment";

        private var _scheduledEquipment:Equipment;
//		private var _scheduledActionID:String;
//		private var _scheduledAction:Equipment;
//		private var _scheduleGroupID:String;
		
		public function EquipmentScheduleItem():void
		{

		}

        override public function initFromReportXML(scheduleItemReportXml:XML, scheduleItemElementName:String):void
        {
			default xml namespace = "http://indivo.org/vocab/xml/documents#";
            super.initFromReportXML(scheduleItemReportXml, scheduleItemElementName);
        }

        override public function createXmlDocument():XML
        {
            var equipmentScheduleItemXml:XML = <EquipmentScheduleItem/>;
            return equipmentScheduleItemXml;
        }

        override public function schedueItemType():String
        {
            return EQUIPMENT;
        }

        override public function addExtraXml(scheduleItemXml:XML):XML
        {
            return scheduleItemXml;
        }

        override public function getScheduleActionId():String
        {
            return _scheduledEquipment.meta.id;
        }
		
        public function get scheduledEquipment():Equipment
        {
            return _scheduledEquipment;
        }

        public function set scheduledEquipment(value:Equipment):void
        {
            _scheduledEquipment = value;
        }
    }
}